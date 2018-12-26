#!/bin/sh

(cd app1; gradle clean build)
(cd app2; gradle clean build)
(cd registry; gradle clean build)
(cd gateway; gradle clean build)

