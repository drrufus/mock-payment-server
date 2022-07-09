IMAGE := drrufus/mock-payment-service
ROOT_DIR := $(shell dirname $(realpath $(firstword $(MAKEFILE_LIST))))

.PHONY: build-image
build-image:
	docker build -t ${IMAGE} .

.PHONY: push-image
push-image:
	docker push ${IMAGE}

.PHONY: deploy
deploy:
	docker run --rm -v ${ROOT_DIR}/deploy:/tmp/ -e K8S_TOKEN=${K8S_TOKEN} 2gis/k8s-handle k8s-handle deploy -s prod

.PHONY: destroy
destroy:
	docker run --rm -v ${ROOT_DIR}/deploy:/tmp/ -e K8S_TOKEN=${K8S_TOKEN} 2gis/k8s-handle k8s-handle destroy -s prod
