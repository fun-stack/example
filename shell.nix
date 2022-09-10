{ pkgs ? import <nixpkgs> {} }:

let
  pname = "fun";
in
  pkgs.mkShell {
    nativeBuildInputs = with pkgs; [
      git
      terraform

      nodejs-16_x
      yarn

      sbt
    ];

    shellHook = with pkgs; ''
      echo --- Welcome to ${pname}! ---
    '';
  }
