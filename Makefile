IMAGE := drrufus/mock-payment-service
ROOT_DIR := $(shell dirname $(realpath $(firstword $(MAKEFILE_LIST))))

.PHONY: build-image
build-image:
	docker build -t ${IMAGE} .

.PHONY: push-image
push-image:
	docker push ${IMAGE}

.PHONY: delete-image
delete-image:
	docker rmi ${IMAGE}

.PHONY: deploy
deploy: token-required
	docker run --rm -v ${ROOT_DIR}/deploy:/tmp/ -e K8S_TOKEN=${K8S_TOKEN} 2gis/k8s-handle k8s-handle deploy -s prod

.PHONY: destroy
destroy: token-required
	docker run --rm -v ${ROOT_DIR}/deploy:/tmp/ -e K8S_TOKEN=${K8S_TOKEN} 2gis/k8s-handle k8s-handle destroy -s prod

guard-%:
	@ if [ "${${*}}" = "" ]; then \
		echo "Environment variable $* not set"; \
		exit 1; \
	fi

token-required: guard-K8S_TOKEN