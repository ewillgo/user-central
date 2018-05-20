package org.trianglex.usercentral.client;

import org.springframework.cloud.openfeign.FeignClient;

import static org.trianglex.usercentral.client.constant.ClientConstant.SERVICE_NAME;

@FeignClient(SERVICE_NAME)
public interface UserClient {


}
