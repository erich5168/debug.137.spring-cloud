# Api-Gateway

Enabled both automatic route + manual route mapping.  You can access `API-USER` with or without `/API-USER/` application name in the URL

```
# Automaic route mapping
http://localhost:8082/api-user/users/status/ok

# Manual route mapping
http://localhost:8082/users/status/ok
```


## Known issue

Known issue only with M1 macs.

[StackOverflow - netty-resolver-dns-native-macos](
https://stackoverflow.com/questions/71966221/java-lang-unsatisfiedlinkerror-no-netty-resolver-dns-native-macos-aarch-64)

```
Suppressed: java.lang.UnsatisfiedLinkError: no netty_resolver_dns_native_macos_aarch_64 in java.library.path: /Users/erichuang/Library/Java/Extensions:/Library/Java/Extensions:/Network/Library/Java/Extensions:/System/Library/Java/Extensions:/usr/lib/java:.
```
