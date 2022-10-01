FROM clojure:lein
WORKDIR /usr/src/app/
# RUN lein deps
RUN apt update
RUN apt install ocl-icd-opencl-dev -y
CMD lein repl :headless
