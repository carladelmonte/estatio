/*
 *
 *  Copyright 2012-2014 Eurocommercial Properties NV
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.estatio.integtests.financial;

import javax.inject.Inject;

import org.estatio.dom.financial.FinancialAccountTransactions;
import org.estatio.fixture.EstatioBaseLineFixture;
import org.estatio.integtests.EstatioIntegrationTest;
import org.junit.Before;
import org.junit.Ignore;

import org.apache.isis.applib.fixturescripts.FixtureScript;

public class FinancialAccountTransactionsTest_findByFinancialAccount extends EstatioIntegrationTest {

    @Before
    public void setupData() {
        scenarioExecution().install(new FixtureScript() {
            @Override
            protected void execute(ExecutionContext executionContext) {
                execute(new EstatioBaseLineFixture(), executionContext);

            }
        });
    }

    @Inject
    private FinancialAccountTransactions financialAccountTransactions;

    @Ignore
    public void forAccount() {
        // then
    }

}