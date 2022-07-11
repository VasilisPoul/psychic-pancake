FROM clojure:lein
WORKDIR /usr/src/app/
# RUN lein deps
CMD lein repl :headless
