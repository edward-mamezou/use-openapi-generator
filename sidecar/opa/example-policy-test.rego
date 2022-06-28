package envoy.authz

test_post_allowed {
    allow with input as { "attributes": {
        "request": {
            "http": {
                "path": "/example/hibernation-pod/id-001/hello",
                "headers": {
                    "payload": "eyJpc3MiOiJodHRwOi8vbG9jYWxob3N0IiwiY3VzdG9tOmZpcnN0bmFtZSI6IkphbWVzIiwiYXVkIjoiQVBQQ0xJRU5USUQiLCJleHAiOjE2NTQ3NTg3NTcsImN1c3RvbTp0eXBlIjoiSHVtYW4ifQ==" }
                }
            }
        }
    }
}
