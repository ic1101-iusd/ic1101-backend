#!/usr/bin/env bash
docker build . -t icp-liquidator-g
docker run -it  -e ICP_NETWORK='http://host.docker.internal:8001' \
-e PROTOCOL_PRINCIPAL='rkp4c-7iaaa-aaaaa-aaaca-cai'  \
-e BTC_TOKEN_PRINCIPAL='ryjl3-tyaaa-aaaaa-aaaba-cai' \
-e MINT_TOKEN_PRINCIPAL='r7inp-6aaaa-aaaaa-aaabq-cai' \
-e INIT_APPROVE='true' \
-e AMOUNT_BTC='200' \
-e AMOUNT_TOKEN='10000' \
-t icp-liquidator-g -p 8080:8080
