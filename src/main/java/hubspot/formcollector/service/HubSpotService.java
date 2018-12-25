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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Service responsible for submitting forms to HubSpot.
 */
@Service
public class HubSpotService {
    private static final Logger LOG = LoggerFactory.getLogger(HubSpotService.class);

    @Value("${hubspot.accountId}")
    private String accountId;

    @Value("${hubspot.apiKey}")
    private String apiKey;

    @Value("${hubspot.formId}")
    private String formId;

    private final WebClient client;

    public HubSpotService() {
        final String url = "https://forms.hubspot.com/uploads/form/v2/" + accountId + "/" + formId;

        this.client = WebClient.builder()
                .baseUrl(url)
                .build();
    }
}
