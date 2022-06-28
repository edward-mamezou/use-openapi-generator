使い方
===

## 準備

このディレクトリ (`sidecar`)にある、application.yaml.examble ファイルを application.yaml ファイルとしてコピーします。

MQTT Broker をホスト PC 以外で使う場合は、次の部分を変更してください。

```yaml
    broker-url: tcp://host.docker.internal:1883
```

以下の部分を Keycloak で使用しているドメイン名に変更してください。

```yaml
    authorizationEndpoint: https://keycloak.example.com/auth/realms/passengers/protocol/openid-connect/auth
    tokenEndpoint: https://keycloak.example.com/auth/realms/passengers/protocol/openid-connect/token
```

`sidecar/envoy` ディレクトリにある、front-envoy-authn-authz.yaml.example ファイルを front-envoy.yaml ファイルとしてコピーします。

51 行目、57 行目、113 行目 を Keycloak で使用しているドメイン名に変更してください。

```text
 51:                          issuer: "https://keycloak.example.com/auth/realms/passengers"
                            
 57:                           uri: "https://keycloak.example.com/auth/realms/passengers/protocol/openid-connect/certs"

113:                          address: keycloak.example.com
```

## 実行

`docker-compose up -d` コマンドで実行します。
