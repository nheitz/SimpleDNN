/* Copyright 2016-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.simplednn.core.functionalities.regularization

import com.kotlinnlp.simplednn.core.arrays.ParamsArray

/**
 * L2 regularization method.
 *
 * @param lambda regularization parameter
 */
class L2Regularization(private val lambda: Double) : ParamsRegularization {

  /**
   * Apply the regularization to given parameters.
   *
   * w = (1 - lambda) * w
   *
   * @param params the parameters to regularize
   */
  override fun apply(params: ParamsArray) {
    params.values.assignProd(1 - this.lambda)
  }
}
