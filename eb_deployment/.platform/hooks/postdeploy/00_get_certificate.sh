#!/usr/bin/env bash

CERTBOT_DOMAIN="INSERT_YOUR_ELASTIC_BEANSTALK_DOMAIN_HERE"
CERTBOT_EMAIL="INSERT_YOUR_EMAIL_HERE"

if ! grep -q letsencrypt </etc/nginx/nginx.conf; then
  sudo certbot -n -d "$CERTBOT_DOMAIN" --nginx --agree-tos --email "$CERTBOT_EMAIL"
fi
