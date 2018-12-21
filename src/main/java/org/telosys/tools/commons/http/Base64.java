/**
 *  Copyright (C) 2008-2017  Telosys project org. ( http://www.telosys.org/ )
 *
 *  Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.telosys.tools.commons.http;

/**
 * Utility class for Base64 encoding/decoding
 * 
 */
public final class  Base64
{
    private static final int  FOUR       =  4;
    private static final int  EIGHT      =  8;
    private static final int  SIXTEEN    = 16;
    private static final int  TWENTYFOUR = 24;
    private static final int  SIGN       = -128;
    private static final byte PAD        = (byte) '=';
    
    private static final int  BASELENGTH     = 255;
    private static final int  LOOKUPLENGTH   = 64;
    private static final byte [] base64Alphabet       = new byte[BASELENGTH];
    private static final byte [] lookUpBase64Alphabet = new byte[LOOKUPLENGTH];

    // Initialization
    static {
        for (int i = 0; i < BASELENGTH; i++ ) {
            base64Alphabet[i] = -1;
        }
        for (int i = 'Z'; i >= 'A'; i--) {
            base64Alphabet[i] = (byte) (i - 'A');
        }
        for (int i = 'z'; i>= 'a'; i--) {
            base64Alphabet[i] = (byte) (i - 'a' + 26);
        }
        for (int i = '9'; i >= '0'; i--) {
            base64Alphabet[i] = (byte) (i - '0' + 52);
        }
        base64Alphabet['+']  = 62;
        base64Alphabet['/']  = 63;
        
        for (int i = 0; i <= 25; i++ ) {
            lookUpBase64Alphabet[i] = (byte) ('A' + i);
        }
        for (int i = 26,  j = 0; i <= 51; i++, j++ ) {
            lookUpBase64Alphabet[i] = (byte) ('a'+ j);
        }
        for (int i = 52,  j = 0; i <= 61; i++, j++ ) {
            lookUpBase64Alphabet[i] = (byte) ('0' + j);
        }
        lookUpBase64Alphabet[62] = (byte) '+';
        lookUpBase64Alphabet[63] = (byte) '/';
    }

    /**
     * Private constructor
     */
    private Base64() {
		super();
	}

    /**
     * Encodes the given string into Base64.
     * @param inputString
     * @return
     */
    public static String encode( String inputString ) {
    	byte[] r = encode(inputString.getBytes());
    	return new String(r);
    }

	/**
     * Encodes the given bytes into Base64.
     *
     * @param inputBytes
     * @return 
     */
    public static byte[] encode( byte[] inputBytes ) {
        int      lengthDataBits  = inputBytes.length*EIGHT;
        int      fewerThan24bits = lengthDataBits%TWENTYFOUR;
        int      numberTriplets  = lengthDataBits/TWENTYFOUR;
        byte     encodedData[]   = null;

        if (fewerThan24bits != 0) {
            //data not divisible by 24 bit
            encodedData = new byte[ (numberTriplets + 1 ) * 4 ];
        }
        else {
            // 16 or 8 bit
            encodedData = new byte[ numberTriplets * 4 ];
        }

        byte k = 0, l = 0, b1 = 0, b2 = 0, b3 = 0;

        int encodedIndex = 0;
        int dataIndex   = 0;
        int i           = 0;
        for ( i = 0; i < numberTriplets; i++ ) {
            dataIndex = i*3;
            b1 = inputBytes[dataIndex];
            b2 = inputBytes[dataIndex + 1];
            b3 = inputBytes[dataIndex + 2];

            l  = (byte)(b2 & 0x0f);
            k  = (byte)(b1 & 0x03);

            encodedIndex = i * 4;
            byte val1 = ((b1 & SIGN)==0)?(byte)(b1>>2):(byte)((b1)>>2^0xc0);
            byte val2 = ((b2 & SIGN)==0)?(byte)(b2>>4):(byte)((b2)>>4^0xf0);
            byte val3 = ((b3 & SIGN)==0)?(byte)(b3>>6):(byte)((b3)>>6^0xfc);

            encodedData[encodedIndex]   = lookUpBase64Alphabet[ val1 ];
            encodedData[encodedIndex+1] =
                lookUpBase64Alphabet[ val2 | ( k<<4 )];
            encodedData[encodedIndex+2] =
                lookUpBase64Alphabet[ (l <<2 ) | val3 ];
            encodedData[encodedIndex+3] = lookUpBase64Alphabet[ b3 & 0x3f ];
        }

        // form integral number of 6-bit groups
        dataIndex    = i*3;
        encodedIndex = i*4;
        if (fewerThan24bits == EIGHT ) {
            b1 = inputBytes[dataIndex];
            k = (byte) ( b1 &0x03 );
            byte val1 = ((b1 & SIGN)==0)?(byte)(b1>>2):(byte)((b1)>>2^0xc0);
            encodedData[encodedIndex]     = lookUpBase64Alphabet[ val1 ];
            encodedData[encodedIndex + 1] = lookUpBase64Alphabet[ k<<4 ];
            encodedData[encodedIndex + 2] = PAD;
            encodedData[encodedIndex + 3] = PAD;
        }
        else if (fewerThan24bits == SIXTEEN) {

            b1 = inputBytes[dataIndex];
            b2 = inputBytes[dataIndex +1 ];
            l = (byte) (b2 & 0x0f);
            k = (byte) (b1 & 0x03);

            byte val1 = ((b1 & SIGN) == 0)?(byte)(b1>>2):(byte)((b1)>>2^0xc0);
            byte val2 = ((b2 & SIGN) == 0)?(byte)(b2>>4):(byte)((b2)>>4^0xf0);

            encodedData[encodedIndex]     = lookUpBase64Alphabet[ val1 ];
            encodedData[encodedIndex + 1] =
                lookUpBase64Alphabet[ val2 | ( k<<4 )];
            encodedData[encodedIndex + 2] = lookUpBase64Alphabet[ l<<2 ];
            encodedData[encodedIndex + 3] = PAD;
        }
        return encodedData;
    }

    /**
     * Decodes the given string into Base64.
     * @param encodedString
     * @return
     */
    public static String decode( String encodedString ) {
    	byte[] r = decode(encodedString.getBytes());
    	return new String(r);
    }

    /**
     * Decodes the given bytes into Base64.
     * 
     * @param encodedBytes 
     * @return 
     */
    public static byte[] decode( byte[] encodedBytes ) {
        // handle the edge case, so we don't have to worry about it later
        if(encodedBytes.length == 0) { return new byte[0]; }

        int      numberQuadruple    = encodedBytes.length/FOUR;
        byte     decodedData[]      = null;
        byte     b1=0,b2=0,b3=0, b4=0, marker0=0, marker1=0;

        // Throw away anything not in base64Data

        int encodedIndex = 0;
        int dataIndex    = 0;
        {
            // this sizes the output array properly - rlw
            int lastData = encodedBytes.length;
            // ignore the '=' padding
            while (encodedBytes[lastData-1] == PAD) {
                if (--lastData == 0) {
                    return new byte[0];
                }
            }
            decodedData = new byte[ lastData - numberQuadruple ];
        }

        for (int i = 0; i < numberQuadruple; i++) {
            dataIndex = i * 4;
            marker0   = encodedBytes[dataIndex + 2];
            marker1   = encodedBytes[dataIndex + 3];

            b1 = base64Alphabet[encodedBytes[dataIndex]];
            b2 = base64Alphabet[encodedBytes[dataIndex +1]];

            if (marker0 != PAD && marker1 != PAD) {
                // No PAD e.g 3cQl
                b3 = base64Alphabet[ marker0 ];
                b4 = base64Alphabet[ marker1 ];

                decodedData[encodedIndex]   = (byte)( b1 <<2 | b2>>4 ) ;
                decodedData[encodedIndex + 1] =
                    (byte)(((b2 & 0xf)<<4 ) |( (b3>>2) & 0xf) );
                decodedData[encodedIndex + 2] = (byte)( b3<<6 | b4 );
            }
            else if (marker0 == PAD) {
                // 2 PAD e.g. 3c[Pad][Pad]
                decodedData[encodedIndex]   = (byte)( b1 <<2 | b2>>4 ) ;
            }
            else if (marker1 == PAD) {
                // 1 PAD e.g. 3cQ[Pad]
                b3 = base64Alphabet[ marker0 ];

                decodedData[encodedIndex]   = (byte)( b1 <<2 | b2>>4 );
                decodedData[encodedIndex + 1] =
                    (byte)(((b2 & 0xf)<<4 ) |( (b3>>2) & 0xf) );
            }
            encodedIndex += 3;
        }
        return decodedData;
    }
}
