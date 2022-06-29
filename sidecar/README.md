使い方
===

## Docker Compose を使う場合

### 準備

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

`sidecar/envoy` ディレクトリにある、front-envoy-docker.yaml.example ファイルを front-envoy.yaml ファイルとしてコピーします。

51 行目、57 行目、113 行目 を Keycloak で使用しているドメイン名に変更してください。

```text
 51:                          issuer: "https://keycloak.example.com/auth/realms/passengers"
                            
 57:                           uri: "https://keycloak.example.com/auth/realms/passengers/protocol/openid-connect/certs"

113:                          address: keycloak.example.com
```

### 実行

`docker compose up -d` コマンドで実行します。

## Kubernetes を使う場合

```shell
kubectl create configmap proxy-config --from-file envoy/front-envoy.yaml
kubectl create secret generic aws --from-file ~/.aws/credentials
kubectl create secret generic opa-policy --from-file opa/example-policy.rego --from-file opa/config.yaml
kubectl create secret generic spring-config --from-file application.yaml
```

```shell
kubectl apply -f deployment.yaml
```

```shell
kubectl port-forward deployment/example-app --address 0.0.0.0 80:8081
```
