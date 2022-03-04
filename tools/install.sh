#!/bin/sh

set -e

USER=${USER:-$(id -u -n)}
# Default settings
VERSION=0.0.2

command_exists() {
  command -v "$@" >/dev/null 2>&1
}

# The [ -t 1 ] check only works when the function is not called from
# a subshell (like in `$(...)` or `(...)`, so this hack redefines the
# function at the top level to always return false when stdout is not
# a tty.
if [ -t 1 ]; then
  is_tty() {
    true
  }
else
  is_tty() {
    false
  }
fi

fmt_error() {
  printf '%sError: %s%s\n' "${FMT_BOLD}${FMT_RED}" "$*" "$FMT_RESET" >&2
}

setup_color() {
  # Only use colors if connected to a terminal
  if ! is_tty; then
    FMT_RED=""
    FMT_BOLD=""
    FMT_RESET=""
    return
  fi

  FMT_RED=$(printf '\033[31m')
  FMT_BOLD=$(printf '\033[1m')
  FMT_RESET=$(printf '\033[0m')
}


setup_solid() {

    command_exists unzip || {
      fmt_error "unzip is not installed"
      exit 1
    }

    command_exists java || {
      fmt_error "java is not installed"
      exit 1
    }

    curl -L https://github.com/kmos/solid/releases/download/v${VERSION}/solid.zip > ~/solid.zip
    unzip ~/solid.zip -d ~/
    mv ~/solid ~/.solid
    rm ~/solid.zip
}

create_symlink() {
  ln -s ~/.solid/app/bin/solid /usr/local/bin
}

main() {

  setup_color
  setup_solid
  create_symlink

  echo "Installation completed..."

}

main "$@"