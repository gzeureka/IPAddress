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

package inet.ipaddr;

import java.util.Iterator;

import inet.ipaddr.format.AddressDivisionSeries;

/**
 * Represents a series of address segments, each of equal byte size, the byte size being a whole number of bytes.
 * 
 * Each segment can potentially range over multiple values, and thus this series of segments can represent many different values as well.
 * 
 * 
 * @author sfoley
 *
 */
public interface AddressSegmentSeries extends AddressDivisionSeries, AddressComponent {
	
	/**
	 * Returns the network object for series of the same version (eg IPv4, IPv6 and MAC each have their own network object)
	 * @return
	 */
	AddressNetwork<?> getNetwork();
	
	/**
	 * Returns the number of segments in this series.
	 * @return
	 */
	int getSegmentCount();
	
	/**
	 * Returns the number of bits comprising each segment in this series.  Segments in the same series are equal length.
	 * @return
	 */
	int getBitsPerSegment();
	
	/**
	 * Returns the number of bytes comprising each segment in this series.  Segments in the same series are equal length.
	 * @return
	 */
	int getBytesPerSegment();
	
	/**
	 * Gets the subsection from the series starting from the given index
	 * 
	 * @throws IndexOutOfBoundsException if index is negative
	 * @param index
	 * @return
	 */
	AddressSection getSection(int index);
	
	/**
	 * Gets the subsection from the series starting from the given index and ending just before the give endIndex
	 * 
	 * @throws IndexOutOfBoundsException if index is negative or endIndex extends beyond the end of the series
	 * @param index
	 * @param endIndex
	 * @return
	 */
	AddressSection getSection(int index, int endIndex);

	/**
	 * Returns the segment from this series at the given index.
	 * 
	 * @throws IndexOutOfBoundsException if the index is negative or as large as the segment count
	 * 
	 * @return
	 */
	AddressSegment getSegment(int index);
	
	/**
	 * Returns the an array with the values of each segment as they would appear in the normalized with wildcards string.
	 * 
	 * @return
	 */
	String[] getSegmentStrings();
	
	/**
	 * Copies the existing segments into the given array.  The array size should be at least as large as {@link #getSegmentCount()} 
	 * 
	 * @throws IndexOutOfBoundsException if the provided array is too small
	 */
	void getSegments(AddressSegment segs[]);
	
	/**
	 * get the segments from start to end and insert into the segs array at the the given index
	 * @param start the first segment index from this series to be included
	 * @param end the segment index after first to be excluded
	 * @param segs the target array
	 * @param index where to insert the segments in the segs array
	 */
	void getSegments(int start, int end, AddressSegment segs[], int index);
	
	/**
	 * Returns the segments of this series of segments as an array.  This must create a new array, so for efficiency use {@link #getSegment(int)} and {@link #getSegmentCount()} instead when feasible.
	 * 
	 * @return
	 */
	AddressSegment[] getSegments();
	
	/**
	 * If this represents a series with ranging values, returns a series representing the lower values of the range.
	 * If this represents an series with a single value in each segment, returns this.
	 * 
	 * @return
	 */
	@Override
	AddressSegmentSeries getLower();
	
	/**
	 * If this represents a series with ranging values, returns a series representing the upper values of the range
	 * If this represents a series with a single value in each segment, returns this.
	 * 
	 * @return
	 */
	@Override
	AddressSegmentSeries getUpper();
	
	@Override
	Iterable<? extends AddressSegmentSeries> getIterable();
	
	/**
	 * Iterates through the individual segment series.
	 * 
	 * The resulting elements will not have an assigned prefix.
	 */
	@Override
	Iterator<? extends AddressSegmentSeries> iterator();
	
	/**
	 * Iterates through the individual segments.
	 */
	Iterator<? extends AddressSegment[]> segmentsIterator();

	/**
	 * Produces the canonical representation of the address
	 * @return
	 */
	String toCanonicalString();

	/**
	 * Produces a short representation of the address while remaining within the confines of standard representation(s) of the address
	 * @return
	 */
	String toCompressedString();
	
	/**
	 * Returns a new segment series with the segments reversed.
	 * 
	 * This does not throw {@link IncompatibleAddressException} since all address series can reverse their segments.
	 * 
	 * @return
	 */
	AddressSegmentSeries reverseSegments();
	
	/**
	 * Returns a new segment series with the bits reversed.
	 * 
	 * @throws IncompatibleAddressException if reversing the bits within a single segment cannot be done 
	 * because the segment represents a range, and when all values in that range are reversed, the result is not contiguous.
	 * 
	 * In practice this means that to be reversible the range must include all values except possibly the largest and/or smallest.
	 * 
	 * @return
	 */
	@Override
	AddressSegmentSeries reverseBits(boolean perByte);

	/**
	 * Returns a new segment series with the bytes reversed.
	 * 
	 * @throws IncompatibleAddressException if the segments have more than 1 bytes, 
	 * and if reversing the bits within a single segment cannot be done because the segment represents a range that is not the entire segment range.
	 * 
	 * @return
	 */
	@Override
	AddressSegmentSeries reverseBytes();
	
	/**
	 * Returns a new segment series with the bytes reversed within each segment.
	 * 
	 * @throws IncompatibleAddressException if the segments have more than 1 bytes, 
	 * and if reversing the bits within a single segment cannot be done because the segment represents a range that is not the entire segment range.
	 * 
	 * @return
	 */
	AddressSegmentSeries reverseBytesPerSegment();
	
	/**
	 * If this series has a prefix length, returns the block for that prefix. Otherwise, this address series is returned.
	 * 
	 * @return the block of address series for the prefix length
	 */
	AddressSegmentSeries toPrefixBlock();
	
	/**
	 * Removes the prefix length.  
	 * <p>
	 * If the series already has a prefix length, the bits previously not within the prefix become zero.
	 * 
	 * @return
	 */
	AddressSegmentSeries removePrefixLength();
	
	/**
	 * Increases or decreases prefix length to the next segment boundary.
	 * <p>
	 * When prefix length is increased, the bits moved within the prefix become zero.
	 * When a prefix length is decreased, whether the bits moved outside the prefix become zero is dependent on the address type.
	 * 
	 * @param nextSegment
	 * @return
	 */
	AddressSegmentSeries adjustPrefixBySegment(boolean nextSegment);
	
	/**
	 * Increases or decreases prefix length by the given increment.
	 * <p>
	 * When prefix length is increased, the bits moved within the prefix become zero.
	 * When the prefix is extended beyond the segment series boundary, it is removed.
	 * When a prefix length is decreased, whether the bits moved outside the prefix become zero is dependent on the address type.
	 * 
	 * @param adjustment
	 * @return
	 */
	AddressSegmentSeries adjustPrefixLength(int adjustment);
	
	/**
	 * Sets the prefix length.
	 * <p>
	 * If this series has a prefix length, and the prefix length is increased, the bits moved within the prefix become zero.
	 * <p>
	 * When the prefix is extended beyond the segment series boundary, it is removed.
	 * <p>
	 * When a prefix length is decreased, whether the bits moved outside the prefix become zero is dependent on the address type.
	 *
	 * @see #applyPrefixLength(int)
	 * @param prefixLength
	 * @return
	 */
	AddressSegmentSeries setPrefixLength(int prefixLength);

	/**
	 * Applies the given prefix length to create a new segment series.
	 * <p>
	 * Similar to {@link #setPrefixLength(int)} except that prefix lengths are never increased. 
	 * When this series already has a prefix length that is less than or equal to the requested prefix length, this series is returned.
	 * <p>
	 * Otherwise the returned series has the given prefix length.
	 * <p>
	 * With some address types, the bits moved outside the prefix will become zero in the returned series.
	 *
	 * @see #setPrefixLength(int)
	 * @param prefixLength
	 * @return
	 */
	AddressSegmentSeries applyPrefixLength(int prefixLength);
}
