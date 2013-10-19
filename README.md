# lein-ivy

A Leiningen plugin to resolve via Ivy.

## About

This plugin is not yet fully functional.  About the only thing that works is
resolution so you can use it to start a repl or develop in emacs.  Creating
uberjars and the like will probably not yet work.

## Usage

You first need to install vine locally as it is not yet on clojars.

      git clone https://github.com/lrenn/vine.git
      cd vine
      lein install

Next, do the same for lein-ivy.

      git clone https://github.com/lrenn/lein-ivy.git
      cd lein-ivy
      lein install

Use this for user-level plugins:

Put `[lein-ivy "0.2.0-SNAPSHOT"]` into the `:plugins` vector of your
`:user` profile.

Use this for project-level plugins:

Put `[lein-ivy "0.1.0-SNAPSHOT"]` into the `:plugins` vector of your project.clj.


You may also need to set the hooks.  This is not supposed to be required by leiningen, but when I first worked on this plugin it was.  Add this to your project.clj.

    :hooks [leiningen.ivy]

Now you can see if it work.

    lein repl

You should see the familiar Ivy resolution and the repl start with your dependencies loaded.  This also works with nrepl in emacs.

## License

Copyright © 2012, 2013 Luke Renn

Distributed under the Eclipse Public License, the same as Clojure.
