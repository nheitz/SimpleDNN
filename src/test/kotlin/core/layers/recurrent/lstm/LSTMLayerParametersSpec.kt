/* Copyright 2016-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package core.layers.recurrent.lstm

import com.kotlinnlp.simplednn.core.functionalities.initializers.ConstantInitializer
import com.kotlinnlp.simplednn.core.functionalities.initializers.RandomInitializer
import com.kotlinnlp.simplednn.core.functionalities.randomgenerators.RandomGenerator
import com.kotlinnlp.simplednn.core.layers.models.recurrent.lstm.LSTMLayerParameters
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.*

/**
 *
 */
class LSTMLayerParametersSpec : Spek({

  describe("a LSTMLayerParameters") {

    context("initialization") {

      context("dense input") {

        var k = 0
        val initValues = doubleArrayOf(
          0.1, 0.2, 0.3, 0.4, 0.5, 0.6,
          0.7, 0.8, 0.9, 1.0, 1.1, 1.2,
          1.3, 1.4, 1.5, 1.6, 1.7, 1.8,
          1.9, 2.0, 2.1, 2.2, 2.3, 2.4,
          2.5, 2.6, 2.7, 2.8, 2.9, 3.0,
          3.1, 3.2, 3.3, 3.4, 3.5, 3.6,
          3.7, 3.8, 3.9, 4.0)
        val randomGenerator = mock<RandomGenerator>()
        whenever(randomGenerator.next()).thenAnswer { initValues[k++] }

        val params = LSTMLayerParameters(
          inputSize = 3,
          outputSize = 2,
          weightsInitializer = RandomInitializer(randomGenerator),
          biasesInitializer = ConstantInitializer(0.9))

        val wIn = params.inputGate.weights.values
        val wOut = params.outputGate.weights.values
        val wFor = params.forgetGate.weights.values
        val wC = params.candidate.weights.values

        val bIn = params.inputGate.biases.values
        val bOut = params.outputGate.biases.values
        val bFor = params.forgetGate.biases.values
        val bC = params.candidate.biases.values

        val wInr = params.inputGate.recurrentWeights.values
        val wOutr = params.outputGate.recurrentWeights.values
        val wForr = params.forgetGate.recurrentWeights.values
        val wCr = params.candidate.recurrentWeights.values

        it("should contain the expected initialized weights of the input gate") {
          (0 until wIn.length).forEach { i -> assertEquals(initValues[i], wIn[i]) }
        }

        it("should contain the expected initialized weights of the output gate") {
          (0 until wOut.length).forEach { i -> assertEquals(initValues[i + 6], wOut[i]) }
        }

        it("should contain the expected initialized weights of the forget gate") {
          (0 until wFor.length).forEach { i -> assertEquals(initValues[i + 12], wFor[i]) }
        }

        it("should contain the expected initialized weights of the candidate") {
          (0 until wC.length).forEach { i -> assertEquals(initValues[i + 18], wC[i]) }
        }

        it("should contain the expected initialized biases of the input gate") {
          (0 until bIn.length).forEach { i -> assertEquals(0.9, bIn[i]) }
        }

        it("should contain the expected initialized biases of the forget gate") {
          (0 until bFor.length).forEach { i -> assertEquals(0.9, bFor[i]) }
        }

        it("should contain the expected initialized biases of the output gate") {
          (0 until bOut.length).forEach { i -> assertEquals(0.9, bOut[i]) }
        }

        it("should contain the expected initialized biases of the candidate") {
          (0 until bC.length).forEach { i -> assertEquals(0.9, bC[i]) }
        }

        it("should contain the expected initialized recurrent weights of the input gate") {
          (0 until wInr.length).forEach { i -> assertEquals(initValues[i + 24], wInr[i]) }
        }

        it("should contain the expected initialized recurrent weights of the output gate") {
          (0 until wOutr.length).forEach { i -> assertEquals(initValues[i + 28], wOutr[i]) }
        }

        it("should contain the expected initialized recurrent weights of the forget gate") {
          (0 until wForr.length).forEach { i -> assertEquals(initValues[i + 32], wForr[i]) }
        }

        it("should contain the expected initialized recurrent weights of the candidate") {
          (0 until wCr.length).forEach { i -> assertEquals(initValues[i + 36], wCr[i]) }
        }
      }

      // TODO: reintroduce tests for sparse input
    }

    context("iteration") {

      val params = LSTMLayerParameters(inputSize = 3, outputSize = 2)

      val iterator = params.iterator()

      val wIn = params.inputGate.weights
      val wOut = params.outputGate.weights
      val wFor = params.forgetGate.weights
      val wC = params.candidate.weights

      val bIn = params.inputGate.biases
      val bOut = params.outputGate.biases
      val bFor = params.forgetGate.biases
      val bC = params.candidate.biases

      val wInr = params.inputGate.recurrentWeights
      val wOutr = params.outputGate.recurrentWeights
      val wForr = params.forgetGate.recurrentWeights
      val wCr = params.candidate.recurrentWeights

      context("iteration 1") {
        it("should return the weights of the input gate") {
          assertEquals(wIn, iterator.next())
        }
      }

      context("iteration 2") {
        it("should return the weights of the output gate") {
          assertEquals(wOut, iterator.next())
        }
      }

      context("iteration 3") {
        it("should return the weights of the forget gate") {
          assertEquals(wFor, iterator.next())
        }
      }

      context("iteration 4") {
        it("should return the weights of the candidate") {
          assertEquals(wC, iterator.next())
        }
      }

      context("iteration 5") {
        it("should return the biases of the input gate") {
          assertEquals(bIn, iterator.next())
        }
      }

      context("iteration 6") {
        it("should return the biases of the output gate") {
          assertEquals(bOut, iterator.next())
        }
      }

      context("iteration 7") {
        it("should return the biases of the forget gate") {
          assertEquals(bFor, iterator.next())
        }
      }

      context("iteration 8") {
        it("should return the biases of the candidate") {
          assertEquals(bC, iterator.next())
        }
      }

      context("iteration 9") {
        it("should return the recurrent weights of the input gate") {
          assertEquals(wInr, iterator.next())
        }
      }

      context("iteration 10") {
        it("should return the recurrent weights of the output gate") {
          assertEquals(wOutr, iterator.next())
        }
      }

      context("iteration 11") {
        it("should return the recurrent weights of the forget gate") {
          assertEquals(wForr, iterator.next())
        }
      }

      context("iteration 12") {
        it("should return the recurrent weights of the candidate") {
          assertEquals(wCr, iterator.next())
        }
      }

      context("iteration 13") {
        it("should return true when calling hasNext()") {
          assertFalse(iterator.hasNext())
        }
      }
    }

    context("copy") {

      val params = LSTMLayerParameters(inputSize = 3, outputSize = 2)
      val clonedParams = params.copy()

      it("should return a new element") {
        assertNotEquals(params, clonedParams)
      }

      it("should return params with same values") {
        assertTrue {
          params.zip(clonedParams).all { it.first.values.equals(it.second.values, tolerance = 1.0e-06) }
        }
      }
    }
  }
})
