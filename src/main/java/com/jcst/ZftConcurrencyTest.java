/*
 * Copyright (c) 2017, Red Hat Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jcst;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.I_Result;

import java.util.concurrent.atomic.AtomicInteger;

// See jcstress-samples or existing tests for API introduction and testing guidelines

/**
 * Description: jcstress hello world
 * All Rights Reserved.
 * @version 1.0 2020/4/23 0023 上午 10:03 by zft
 */
@JCStressTest
@Description(value = "this is a jcsress hello world")
// Outline the outcomes here. The default outcome is provided, you need to remove it:
@Outcome(id = "1", expect = Expect.ACCEPTABLE_INTERESTING, desc = "One update lost.")
@Outcome(id = "2", expect = Expect.ACCEPTABLE, desc = "Both updates.")
@State
public class ZftConcurrencyTest {

    volatile int v;
    // AtomicInteger i = new AtomicInteger();

    @Actor
    public void actor1() {
        // Put the code for first thread here
        v++;
        // i.incrementAndGet();
    }

    @Actor
    public void actor2() {
        // Put the code for second thread here
        v++;
        // i.incrementAndGet();
    }

    @Arbiter
    public void arbiter(I_Result r) {
        r.r1 = v;
        // r.r1 = i.get();
    }

}
