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
import org.springframework.http.HttpCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
        return request.formData()
                .map(fields -> {
                    final HttpCookie trackingCode = request.cookies().getFirst("hubspotutk");
                    final Optional<String> pageName = request.queryParam("pageName");
                    final Optional<InetSocketAddress> ipAddress = request.remoteAddress();
                    final Map<String, String> trackingData = new HashMap<>();

                    if (trackingCode != null) {
                        trackingData.put("trackingCode", trackingCode.getValue());
                    }

                    pageName.ifPresent(s -> trackingData.put("pageName", s));
                    ipAddress.ifPresent(a -> trackingData.put("ipAddress", a.toString()));

                    return Tuples.of(trackingData, fields);
                })
                .flatMap(t -> hubSpotService.submitForm(t.getT1(), t.getT2()))
                .flatMap(aVoid -> ServerResponse.ok().build());
    }
}
