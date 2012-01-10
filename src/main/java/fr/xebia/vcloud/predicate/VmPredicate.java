/*
 * Copyright (c) 2012. - Xebia
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.xebia.vcloud.predicate;

import com.google.common.base.Predicate;
import com.vmware.vcloud.sdk.VM;

import javax.annotation.Nullable;
import javax.management.StringValueExp;

/**
 * User: charles
 * Date: 10/01/12
 * Time: 00:32
 */
public class VmPredicate implements Predicate<VM>{
    private String vmName;

    public VmPredicate(String vmName) {
        this.vmName = vmName;
    }
    
    public boolean apply(@Nullable VM input) {
        return vmName.equals(input.getReference().getName());
    }
}
