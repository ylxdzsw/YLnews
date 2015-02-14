#!/usr/bin/env bash
cd www/src
coffee -c -o .. *.coffee
jade -o .. *.jade
lessc -x main.less ../main.css