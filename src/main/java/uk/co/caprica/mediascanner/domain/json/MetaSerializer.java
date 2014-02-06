/*
 * This file is part of media-scanner.
 *
 * media-scanner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * media-scanner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with media-scanner.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2014 Caprica Software Limited.
 */

package uk.co.caprica.mediascanner.domain.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.common.collect.Multimap;

/**
 *
 */
public class MetaSerializer extends JsonSerializer<Multimap<String,Object>> {

    @Override
    public void serialize(Multimap<String, Object> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartArray();
        for (String key : value.keySet()) {
            jgen.writeStartObject();
            jgen.writeArrayFieldStart(key);
            for (Object obj : value.get(key)) {
                jgen.writeObject(obj);
            }
            jgen.writeEndArray();
            jgen.writeEndObject();
        }
        jgen.writeEndArray();
    }
}