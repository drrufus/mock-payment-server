IMAGE := drrufus/mock-payment-service

.PHONY: build-image
build-image:
	docker build -t ${IMAGE} .

.PHONY: push-image
push-image:
	docker push ${IMAGE}
