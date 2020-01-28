## About
현재 후이즈 도메인을 사용하고 있는 상황에서 Letsencrypt 인증서를 사용할 때
도메인 소유권을 인정받기 위해 SPF(TXT) 레코드를 입력해야 하지만 브라우저를 통해 레코드를 변경할 수 있기 때문에
재발급시 직접 수동으로 해야 하는 문제가 있어 다음과 같은 CLI 툴을 작성했습니다.

## Requirements
- java 1.8

## Install

https://github.com/kkojaeh/whois-co-kr-domain-cli/releases 페이지에서
최신의 whois-co-kr-domain-cli.jar 파일을 다운로드 합니다.

## Usage
```
$ java -jar whois-co-kr-domain-cli.jar
Usage: java -jar whois-co-kr-domain-cli.jar [command] [options]
후이즈(http://whois.co.kr) 도메인의 레코드를 변경합니다.
각 [command] 와 --help 옵션을 이용하여 실행 옵션을 확인하세요
ex) java -jar whois-co-kr-domain-cli.jar add-spf --help
Commands:
  add-spf       add SPF(TXT) record
  delete-spf    delete SPF(TXT) record
  add-mx        add MX record
  delete-mx     delete MX record
  add-a         add A record
  delete-a      delete A record
  add-cname     add CNAME record
  delete-cname  delete CNAME record
  add-ptr       add PTR record
  delete-ptr    delete PTR record
  add-srv       add SRV record
  delete-srv    delete SRV record
```
위와 같이 각 명령을 확인하고 옵션을 확인하고 실행합니다.

### certbot renew

수동으로 인증을 재생성할 때 도메인 소유권 확인을 위한 훅을 사용해야 합니다.
https://certbot.eff.org/docs/using.html#pre-and-post-validation-hooks 을 참조합니다.
아래는 whois-co-kr-domain-cli 를 사용하여 소유권을 인정받는 예제입니다.
```
#!/bin/bash

# 후이즈 사용자 명 과 패스워드
WHOIS_USERNAME="..."
WHOIS_PASSWORD="..."

DOMAIN="$CERTBOT_DOMAIN"

# 후이즈 spf 레코드중 host 가 _acme-challenge 와 일치하는 레코드를 삭제
java -jar whois-co-kr-domain-cli.jar delete-spf --host _acme-challenge
# certbot 이 제공하는 값을 _acme-challenge host 로 추가
java -jar whois-co-kr-domain-cli.jar add-spf --host _acme-challenge --spf $CERTBOT_VALIDATION

# 전파를 위해 대기
sleep 20

```

<!--
release: ./gradlew clean release

-->
