#!/usr/bin/env bash

CERTBOT_DOMAIN="isg-delta.eu-west-1.elasticbeanstalk.com"
CERTBOT_EMAIL="malikhinnawi01@gmail.com"

if ! grep -q letsencrypt </etc/nginx/nginx.conf; then
  sudo certbot -n -d "$CERTBOT_DOMAIN" --nginx --agree-tos --email "$CERTBOT_EMAIL"
fi
