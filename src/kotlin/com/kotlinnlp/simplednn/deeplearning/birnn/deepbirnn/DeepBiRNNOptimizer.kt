/* Copyright 2016-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.simplednn.deeplearning.birnn.deepbirnn

import com.kotlinnlp.simplednn.core.functionalities.updatemethods.UpdateMethod
import com.kotlinnlp.simplednn.core.functionalities.updatemethods.adam.ADAMMethod
import com.kotlinnlp.simplednn.core.optimizer.Optimizer
import com.kotlinnlp.simplednn.deeplearning.birnn.BiRNNOptimizer
import com.kotlinnlp.simplednn.utils.scheduling.BatchScheduling
import com.kotlinnlp.simplednn.utils.scheduling.EpochScheduling
import com.kotlinnlp.simplednn.utils.scheduling.ExampleScheduling

/**
 * The optimizer of the DeepBiRNN which in turn contains BiRNN sub-networks (or layers)
 *
 * @param network the [DeepBiRNN] to optimize
 * @param updateMethod the [UpdateMethod] used for all the BiRNN layers
 */
class DeepBiRNNOptimizer(
  network: DeepBiRNN,
  val updateMethod: UpdateMethod = ADAMMethod(stepSize = 0.0001)
) : Optimizer {

  /**
   * Array of optimizers for all the stacked BiRNN layers.
   */
  private val optimizers = Array(size = network.numberOfLayers, init = {
    BiRNNOptimizer(network = network.layers[it], updateMethod = this.updateMethod)
  })

  /**
   * Update the parameters using the accumulated errors and then reset the errors.
   */
  override fun update(): Unit {
    this.optimizers.forEach { it.update() }
  }

  /**
   * Accumulate the parameters errors into the optimizer.
   *
   * @param errors the parameters errors to accumulate
   */
  fun accumulate(errors: DeepBiRNNParameters) {
    require(errors.paramsPerBiRNN.size == this.optimizers.size) {
      "Required errors.paramsPerBiRNN.size == this.optimizers.size"
    }

    this.optimizers.zip(errors.paramsPerBiRNN).forEach {
      it.first.accumulate(it.second)
    }
  }

  /**
   * Method to call every new epoch.
   * In turn it calls the same method into the `updateMethod`
   */
  override fun newEpoch() {

    if (this.updateMethod is EpochScheduling) {
      this.updateMethod.newEpoch()
    }
  }

  /**
   * Method to call every new batch.
   * In turn it calls the same method into the `updateMethod`
   */
  override fun newBatch() {

    if (this.updateMethod is BatchScheduling) {
      this.updateMethod.newBatch()
    }
  }

  /**
   * Method to call every new example.
   * In turn it calls the same method into the `updateMethod`
   */
  override fun newExample() {

    if (this.updateMethod is ExampleScheduling) {
      this.updateMethod.newExample()
    }
  }
}