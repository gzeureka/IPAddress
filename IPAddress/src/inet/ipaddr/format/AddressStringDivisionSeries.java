/*
 * Copyright 2017 Sean C Foley
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     or at
 *     https://github.com/seancfoley/IPAddress/blob/master/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package inet.ipaddr.format;

import java.io.Serializable;

/**
 * A generic part of an address for the purpose of producing a representative string.  
 * It is divided into a series of combinations of individual address string divisions ({@link AddressStringDivision}).
 * The number of such series is the division count.
 * 
 * @author sfoley
 *
 */
public interface AddressStringDivisionSeries extends Serializable {
	
	AddressStringDivision getDivision(int index);
	
	int getDivisionCount();
}
