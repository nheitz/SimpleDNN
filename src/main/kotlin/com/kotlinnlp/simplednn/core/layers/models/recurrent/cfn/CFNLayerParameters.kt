/* Copyright 2016-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.simplednn.core.layers.models.recurrent.cfn

import com.kotlinnlp.simplednn.core.arrays.ParamsArray
import com.kotlinnlp.simplednn.core.functionalities.initializers.GlorotInitializer
import com.kotlinnlp.simplednn.core.functionalities.initializers.Initializer
import com.kotlinnlp.simplednn.core.layers.models.recurrent.RecurrentLinearParams
import com.kotlinnlp.simplednn.core.layers.LayerParameters

/**
 * The parameters of the layer of type CFN.
 *
 * @property inputSize input size
 * @property outputSize output size
 * @param weightsInitializer the initializer of the weights (zeros if null, default: Glorot)
 * @param biasesInitializer the initializer of the biases (zeros if null, default: Glorot)
 * @param sparseInput whether the weights connected to the input are sparse or not
 */
class CFNLayerParameters(
  inputSize: Int,
  outputSize: Int,
  weightsInitializer: Initializer? = GlorotInitializer(),
  biasesInitializer: Initializer? = GlorotInitializer(),
  private val sparseInput: Boolean = false
) : LayerParameters(
  inputSize = inputSize,
  outputSize = outputSize,
  weightsInitializer = weightsInitializer,
  biasesInitializer = biasesInitializer
) {

  companion object {

    /**
     * Private val used to serialize the class (needed by Serializable)
     */
    @Suppress("unused")
    private const val serialVersionUID: Long = 1L
  }

  /**
   *
   */
  val inputGate = RecurrentLinearParams(
    inputSize = this.inputSize,
    outputSize = this.outputSize,
    sparseInput = this.sparseInput)

  /**
   *
   */
  val forgetGate = RecurrentLinearParams(
    inputSize = this.inputSize,
    outputSize = this.outputSize,
    sparseInput = this.sparseInput)

  /**
   *
   */
  val candidateWeights = ParamsArray(dim1 = this.outputSize, dim2 = this.inputSize)

  /**
   * The list of weights parameters.
   */
  override val weightsList: List<ParamsArray> = listOf(

    this.inputGate.weights,
    this.forgetGate.weights,
    this.candidateWeights,

    this.inputGate.recurrentWeights,
    this.forgetGate.recurrentWeights
  )

  /**
   * The list of biases parameters.
   */
  override val biasesList: List<ParamsArray> = listOf(
    this.inputGate.biases,
    this.forgetGate.biases
  )

  /**
   * Initialize all parameters values.
   */
  init {
    this.initialize()
  }
}
