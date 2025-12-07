package com.jminiapp.examples.ceasar;

import com.jminiapp.core.adapters.JSONAdapter;

/**
 * JSON adapter for CeasarCipherState objects.
 *
 * <p>This adapter enables the Caesar Cipher app to import and export state
 * to/from JSON files.</p>
 */
public class CeasarJSONAdapter implements JSONAdapter<CeasarCipherState> {

    @Override
    public Class<CeasarCipherState> getstateClass() {
        return CeasarCipherState.class;
    }
}
