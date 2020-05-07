/* Copyright 2020-present Simone Cangialosi. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package bert.training

import com.kotlinnlp.neuraltokenizer.NeuralTokenizer
import com.kotlinnlp.neuraltokenizer.NeuralTokenizerModel
import com.kotlinnlp.simplednn.core.embeddings.EmbeddingsMap
import com.kotlinnlp.simplednn.core.functionalities.updatemethods.adam.ADAMMethod
import com.kotlinnlp.simplednn.deeplearning.transformers.BERTModel
import com.kotlinnlp.simplednn.deeplearning.transformers.BERTTrainer
import com.kotlinnlp.utils.DictionarySet
import java.io.File
import java.io.FileInputStream

/**
 * Train a BERT model.
 *
 * Launch with the '-h' option for help about the command line arguments.
 */
fun main(args: Array<String>) {

  val parsedArgs = CommandLineArguments(args)

  val tokenizer = NeuralTokenizer(model = parsedArgs.tokenizerModelPath.let {
    println("Reading tokenizer model from '$it'...")
    NeuralTokenizerModel.load(FileInputStream(it))
  })

  val vocabulary: DictionarySet<String> = parsedArgs.vocabularyPath.let {
    println("Reading vocabulary from '$it'...")
    readVocabulary(filename = parsedArgs.vocabularyPath, minOccurrences = 100, maxTerms = 20000)
  }

  val embeddingsMap: EmbeddingsMap<String> = parsedArgs.embeddingsPath?.let {
    println("Loading pre-trained word embeddings from '$it'...")
    EmbeddingsMap.load(it)
  } ?: EmbeddingsMap(size = 100)

  File(parsedArgs.datasetPath).useLines { examples ->

    println("Reading training set from '${parsedArgs.datasetPath}'...")

    val model = BERTModel(
      inputSize = embeddingsMap.size,
      attentionSize = embeddingsMap.size / 3,
      attentionOutputSize = embeddingsMap.size / 3,
      outputHiddenSize = 2048,
      numOfHeads = 3,
      numOfLayers = 3,
      dropout = 0.15,
      vocabulary = vocabulary,
      wordEmbeddings = parsedArgs.embeddingsPath?.let { embeddingsMap })

    val helper = BERTTrainer(
      model = model,
      modelFilename = parsedArgs.modelPath,
      tokenizer = tokenizer,
      updateMethod = ADAMMethod(stepSize = 0.001),
      termsDropout = 0.15,
      optimizeEmbeddings = parsedArgs.embeddingsPath == null,
      examples = examples.asIterable(),
      shuffler = null,
      epochs = parsedArgs.epochs)

    println("\n-- Start training")
    helper.train()
  }
}

/**
 * Read the vocabulary for the BERT training from file.
 * Each line of the file must contain a term and its occurrences, separated by a tab char (`\t`).
 * On top of this, punctuation terms are inserted in the dictionary.
 *
 * @param filename the filename of the vocabulary
 * @param minOccurrences the min number of occurrences to insert a term into the vocabulary
 * @param maxTerms the max number of terms to insert into the vocabulary or null for no limit
 *
 * @return a vocabulary for the training
 */
private fun readVocabulary(filename: String, minOccurrences: Int, maxTerms: Int? = null): DictionarySet<String> {

  val terms: List<String> = File(filename)
    .readLines()
    .asSequence()
    .map { it.split("\t") }
    .map { it[0] to it[1].toInt() }
    .filter { it.second >= minOccurrences }
    .sortedByDescending { it.second }
    .map { it.first }
    .toList()

  return DictionarySet(terms.take(maxTerms ?: terms.size))
}
