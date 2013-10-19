# lein-ivy

A Leiningen plugin to resolve via Ivy.

## Usage

You first need to install vine locally as it is not yet on clojars.

      git clone https://github.com/lrenn/vine.git
      cd vine
      lein install

Use this for user-level plugins:

Put `[lein-ivy "0.2.0-SNAPSHOT"]` into the `:plugins` vector of your
`:user` profile.

Use this for project-level plugins:

Put `[lein-ivy "0.1.0-SNAPSHOT"]` into the `:plugins` vector of your project.clj.


## License

Copyright © 2012 FIXME

Distributed under the Eclipse Public License, the same as Clojure.
