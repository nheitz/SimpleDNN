/* Copyright 2016-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package deeplearning.birnn.utils

import com.kotlinnlp.simplednn.core.functionalities.activations.Sigmoid
import com.kotlinnlp.simplednn.core.functionalities.activations.Tanh
import com.kotlinnlp.simplednn.core.layers.LayerType
import com.kotlinnlp.simplednn.core.layers.feedforward.FeedforwardLayerParameters
import com.kotlinnlp.simplednn.core.layers.recurrent.simple.SimpleRecurrentLayerParameters
import com.kotlinnlp.simplednn.deeplearning.birnn.BiRNN
import com.kotlinnlp.simplednn.simplemath.ndarray.dense.DenseNDArray
import com.kotlinnlp.simplednn.simplemath.ndarray.dense.DenseNDArrayFactory

/**
 *
 */
object BiRNNEncoderUtils {

  /**
   *
   */
  fun buildInputSequence(): Array<DenseNDArray> = arrayOf(
    DenseNDArrayFactory.arrayOf(doubleArrayOf(0.5, 0.6)),
    DenseNDArrayFactory.arrayOf(doubleArrayOf(0.7, -0.4)),
    DenseNDArrayFactory.arrayOf(doubleArrayOf(0.0, -0.7))
  )

  /**
   *
   */
  fun buildOutputErrorsSequence(): Array<DenseNDArray> = arrayOf(
    DenseNDArrayFactory.arrayOf(doubleArrayOf(0.7, -0.2)),
    DenseNDArrayFactory.arrayOf(doubleArrayOf(-0.7, 0.0)),
    DenseNDArrayFactory.arrayOf(doubleArrayOf(-0.4, -0.9))
  )

  /**
   *
   */
  fun buildBiRNN(): BiRNN {

    val birnn = BiRNN(
      inputLayerSize = 2,
      inputType = LayerType.Input.Dense,
      hiddenLayerSize = 3,
      hiddenLayerActivation = Tanh(),
      hiddenLayerConnectionType = LayerType.Connection.SimpleRecurrent,
      outputLayerSize = 2,
      outputLayerActivation = Sigmoid()
    )

    this.initL2RParameters(params = birnn.leftToRightNetwork.model.paramsPerLayer[0] as SimpleRecurrentLayerParameters)
    this.initR2LParameters(params = birnn.rightToLeftNetwork.model.paramsPerLayer[0] as SimpleRecurrentLayerParameters)
    this.initOutputParameters(params = birnn.outputNetwork.model.paramsPerLayer[0] as FeedforwardLayerParameters)

    return birnn
  }

  /**
   *
   */
  private fun initL2RParameters(params: SimpleRecurrentLayerParameters) {

    params.unit.weights.values.assignValues(DenseNDArrayFactory.arrayOf(arrayOf(
      doubleArrayOf(-0.9, 0.4),
      doubleArrayOf(0.7, -1.0),
      doubleArrayOf(-0.9, -0.4)
    )))

    params.unit.biases.values.assignValues(DenseNDArrayFactory.arrayOf(doubleArrayOf(0.4, -0.3, 0.8)))

    params.unit.recurrentWeights.values.assignValues(DenseNDArrayFactory.arrayOf(arrayOf(
      doubleArrayOf(0.1, 0.9, -0.5),
      doubleArrayOf(-0.6, 0.7, 0.7),
      doubleArrayOf(0.3, 0.9, 0.0)
    )))
  }

  /**
   *
   */
  private fun initR2LParameters(params: SimpleRecurrentLayerParameters) {

    params.unit.weights.values.assignValues(DenseNDArrayFactory.arrayOf(arrayOf(
      doubleArrayOf(0.3, 0.1),
      doubleArrayOf(0.6, 0.0),
      doubleArrayOf(-0.7, 0.1)
    )))

    params.unit.biases.values.assignValues(DenseNDArrayFactory.arrayOf(doubleArrayOf(0.2, -0.9, -0.2)))

    params.unit.recurrentWeights.values.assignValues(DenseNDArrayFactory.arrayOf(arrayOf(
      doubleArrayOf(-0.2, 0.7, 0.7),
      doubleArrayOf(-0.2, 0.0, -1.0),
      doubleArrayOf(0.5, -0.4, 0.4)
    )))
  }

  /**
   *
   */
  private fun initOutputParameters(params: FeedforwardLayerParameters) {

    params.unit.weights.values.assignValues(DenseNDArrayFactory.arrayOf(arrayOf(
      doubleArrayOf(0.3, 0.1, 0.6, -0.7, 0.3, -1.0),
      doubleArrayOf(0.6, 0.0, 0.6, 0.8, -0.6, 0.4)
    )))

    params.unit.biases.values.assignValues(DenseNDArrayFactory.arrayOf(doubleArrayOf(0.2, -0.9)))
  }
}