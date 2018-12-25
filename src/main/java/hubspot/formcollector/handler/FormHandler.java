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
package hubspot.formcollector.handler;

import hubspot.formcollector.service.HubSpotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class FormHandler {
    private static final Logger LOG = LoggerFactory.getLogger(FormHandler.class);

    @Autowired
    private HubSpotService hubSpotService;

    /**
     * Submit a generic form to HubSpot.
     *
     * @param request http request
     * @return http response
     */
    public Mono<ServerResponse> formSubmit(ServerRequest request) {
        return null;
    }
}
