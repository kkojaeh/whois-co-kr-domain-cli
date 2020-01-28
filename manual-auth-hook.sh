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
