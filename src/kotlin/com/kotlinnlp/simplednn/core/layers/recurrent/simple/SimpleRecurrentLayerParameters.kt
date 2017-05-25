/* Copyright 2016-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.simplednn.core.layers.recurrent.simple

import com.kotlinnlp.simplednn.core.layers.LayerParameters
import com.kotlinnlp.simplednn.core.functionalities.randomgenerators.RandomGenerator

/**
 * The parameters of the layer of type SimpleRecurrent.
 *
 * @property inputSize input size
 * @property outputSize output size
 * @property sparseInput whether the weights connected to the input are sparse or not
 */
class SimpleRecurrentLayerParameters(
  inputSize: Int,
  outputSize: Int,
  private val sparseInput: Boolean = false
) : LayerParameters(inputSize = inputSize, outputSize = outputSize) {

  /**
   *
   */
  val weights =this.buildUpdatableArray(
    dim1 = this.outputSize,
    dim2 = this.inputSize,
    sparseInput = this.sparseInput)

  /**
   *
   */
  val biases = this.buildUpdatableArray(this.outputSize)

  /**
   *
   */
  val recurrentWeights = this.buildUpdatableArray(dim1 = this.outputSize, dim2 = this.inputSize)

  /**
   *
   */
  init {
    this.paramsList = arrayListOf(
      this.weights,
      this.biases,
      this.recurrentWeights
    )
  }

  /**
   *
   * @param randomGenerator randomGenerator
   * @param biasesInitValue biasesInitValue
   * @return
   */
  override fun initialize(randomGenerator: RandomGenerator, biasesInitValue: Double) {
    this.weights.values.randomize(randomGenerator)
    this.biases.values.assignValues(biasesInitValue)
    this.recurrentWeights.values.randomize(randomGenerator)
  }
}
