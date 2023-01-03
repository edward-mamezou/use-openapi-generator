package envoy.authz

import input.attributes.request.http as http_request

default allow = false

payload := payload {
    jwt_payload := http_request.headers["payload"]
    payload := json.unmarshal(base64url.decode(jwt_payload))
}

allow := action_allowed {
    re_match("^\/example\/hibernation-pod\/.*\/hello.*$", http_request.path)
    payload["custom:type"] == "Human"
    action_allowed := {
        "allowed": true
    }
}
