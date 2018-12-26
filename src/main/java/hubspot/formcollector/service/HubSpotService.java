/**
 * Copyright 2018 Greg Whitaker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package hubspot.formcollector.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Service responsible for submitting forms to HubSpot.
 */
@Service
public class HubSpotService {
    private static final Logger LOG = LoggerFactory.getLogger(HubSpotService.class);

    @Value("${hubspot.portalId}")
    private String portalId;

    @Value("${hubspot.formId}")
    private String formId;

    @Autowired
    private WebClient client;

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Submits the form data to HubSpot.
     *
     * @param trackingData HubSpot tracking cookie data
     * @param data form data to submit
     * @return an empty {@link Mono}
     */
    public Mono<Void> submitForm(Map<String, String> trackingData, MultiValueMap<String, String> data) {
        data.add("hs_context", hsContext(trackingData));
        data.add("hs_lead_status", "NEW");

        return client.post()
                .uri("/uploads/form/v2/{portalId}/{formId}", portalId, formId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(data))
                .retrieve()
                .bodyToMono(Void.class);
    }

    /**
     * Creates the HubSpot context data sent in the "hs_context" form value.
     *
     * @param trackingData HubSpot tracking cookie data
     * @return data sent in the "hs_context" form value
     */
    private String hsContext(Map<String, String> trackingData) {
        Map<String, String> data = new HashMap<>();
        data.put("hutk", trackingData.get("trackingCode"));
        data.put("ipAddress", trackingData.get("ipAddress"));
        data.put("pageName", trackingData.get("pageName"));

        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            LOG.error("Unable to process tracking data", e);
            return null;
        }
    }
}
