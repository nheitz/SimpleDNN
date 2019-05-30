/* Copyright 2016-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package core.layers

import com.kotlinnlp.simplednn.core.arrays.AugmentedArray
import com.kotlinnlp.simplednn.core.functionalities.activations.Tanh
import com.kotlinnlp.simplednn.core.layers.LayerType
import com.kotlinnlp.simplednn.core.layers.models.feedforward.simple.FeedforwardLayerParameters
import com.kotlinnlp.simplednn.core.layers.models.feedforward.simple.FeedforwardLayer
import com.kotlinnlp.simplednn.simplemath.equals
import com.kotlinnlp.simplednn.simplemath.ndarray.Shape
import com.kotlinnlp.simplednn.simplemath.ndarray.dense.DenseNDArrayFactory
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertTrue

/**
 *
 */
class LayerStructureSpec : Spek({

  describe("A generic Layer") {

    context("initialization with dropout") {

      context("forward") {

        val inputArray = AugmentedArray(DenseNDArrayFactory.random(shape = Shape(100000)))
        val inputArrayCopy = inputArray.clone()

        val layer = FeedforwardLayer(
          inputArray = inputArray,
          inputType = LayerType.Input.Dense,
          outputArray = AugmentedArray.zeros(5),
          params = FeedforwardLayerParameters(inputSize = 100000, outputSize = 5),
          activationFunction = Tanh(),
          dropout = 0.25)

        layer.forward(useDropout = true)

        val zerosCount = (0 until inputArray.size).count { i -> inputArray.values[i] == 0.0 }

        it("should contain the expected number of dropped values") {
          // WARNING: This test is based on random generation of values
          assertTrue { equals(25000.0, zerosCount.toDouble(), tolerance=500.0) }
        }

        it("should contain the expected kept values") {
          assertTrue {
            (0 until inputArray.size).all { i ->
              val value = inputArray.values[i]
              val origValue = inputArrayCopy.values[i]

              value == 0.0 || equals(origValue / 0.75, value, tolerance=1.0e-08)
            }
          }
        }
      }
    }
  }
})
