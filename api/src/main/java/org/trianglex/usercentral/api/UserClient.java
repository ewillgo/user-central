package org.trianglex.usercentral.api;

import org.springframework.cloud.openfeign.FeignClient;

import static org.trianglex.usercentral.api.constant.ClientConstant.SERVICE_NAME;

@FeignClient(SERVICE_NAME)
public interface UserClient {


}
