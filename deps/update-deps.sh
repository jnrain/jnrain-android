#!/bin/sh

PROJECT_NAME="jnrain-android"


# Commands
GIT_CMD="${GIT_CMD:-git}"
MVN_CMD="${MVN_CMD:-mvn}"
MVN30_CMD="${MVN30_CMD:-mvn-3.0}"


# Semi-hardcoded dependencies
use_custom_cy=true
cy_name=Cytosol
cy_repo_author=xen0n
cy_ver=master
cy_pull=true


# Helpers
echoinfo () {
    printf "\033[1;32m * \033[m[${PROJECT_NAME}] $@\n" ;
}

# stderr Helper
# http://stackoverflow.com/questions/2990414/echo-that-outputs-to-stderr
echoerr () {
    printf "\033[1;31m * \033[m[${PROJECT_NAME}] $@\n" >&2;
}


errexit () {
    exitcode=$1
    reason=$2

    echoerr ""
    echoerr "\033[1;31m * \033[mBailing out: ${reason}"
    echoerr ""

    exit "${exitcode}"
}


# Maven version
mvn31_checked=false
mvn30_checked=false


mvn_version () {
    "${MVN_CMD}" --version | head -1
}


mvn30_version () {
    "${MVN30_CMD}" --version | head -1
}


mvn_version_check () {
    if mvn31_checked; then
        true
    else
        _mvnver="$( mvn_version )"
        echo "${_mvnver}" | grep '^Apache Maven 3.1' > /dev/null 2>&1 || mvn_version_fail 3.1.x "${_mvnver}"
        mvn31_checked=true
    fi
}


mvn30_version_check () {
    if mvn30_checked; then
        true
    else
        _mvnver="$( mvn30_version )"
        echo "${_mvnver}" | grep '^Apache Maven 3.0' > /dev/null 2>&1 || mvn_version_fail 3.0.x "${_mvnver}"
        mvn30_checked=true
    fi
}


mvn_version_fail () {
    expected_ver=$1
    mvnver=$2

    echoerr "\033[1;31m !!!\033[m Incompatible Maven version detected."
    echoerr ""
    echoerr "Building dependencies currently requires Maven ${expected_ver}, but your"
    echoerr "Maven advertised this instead:"
    echoerr ""
    echoerr "${mvnver}"
    echoerr ""
    echoerr "Please provide the Maven executables via the MVN_CMD environment"
    echoerr "variable, like this:"
    echoerr ""
    echoerr "    $ MVN_CMD=mvn-3.1 MVN30_CMD=mvn-3.0 ./update-deps.sh"

    errexit 2 "Maven version check failed"
}


# Clone or update dependency GitHub repos
gh_repo_sync () {
    repoauthor=$1
    repo=$2

    if [ -d "$repo" ]; then
        # directory exists
        echoinfo "Updating ${repoauthor}/${repo}" ;

        ( cd "$repo" && "${GIT_CMD}" fetch origin ) || errexit 3 "git pull failed" ;
    else
        # directory doesn't exist
        # don't process the rare circumstance where $repo exists but IS A FILE
        # that's bound to fail so don't even attempt to rescue
        echoinfo "Cloning ${repoauthor}/${repo}" ;

        "${GIT_CMD}" clone "https://github.com/${repoauthor}/${repo}.git" || errexit 4 "git clone failed" ;
    fi
}


# Build helpers
git_co () {
    ver=$1
    do_pull=$2

    "${GIT_CMD}" checkout "${ver}" || errexit 5 "git checkout failed"

    if ${do_pull}; then
        "${GIT_CMD}" pull || errexit 6 "git pull failed"
    fi
}


domvn () {
    mvn_version_check
    "${MVN_CMD}" $@ || errexit 7 "domvn failed"
}


domvn30 () {
    mvn30_version_check
    "${MVN30_CMD}" $@ || errexit 8 "domvn30 failed"
}


# Cytosol
build_cy () {
    if ${use_custom_cy}; then
        gh_repo_sync "${cy_repo_author}" "${cy_name}"

        cd "${cy_name}"
        git_co "${cy_ver}" "${cy_pull}"

        # Sync deps of Cytosol
        cd deps || errexit 101 "Cytosol repo directory layout unrecognized"
        echoinfo "Syncing Cytosol dependencies"
        ./update-deps.sh || errexit 102 "Syncing of Cytosol dependencies failed"

        cd ..
        echoinfo "Building and installing Cytosol"
        domvn clean install -q

        cd ..
    else
        echoinfo "Using Cytosol from Maven Central"
    fi
}


# Main part
echoinfo "Using git: ${GIT_CMD}"
echoinfo "Using mvn: ${MVN_CMD}"
echoinfo "Using mvn 3.0.x: ${MVN30_CMD}"
echoinfo ""

build_cy

echoinfo ""
echoinfo "Dependencies successfully set up"
echoinfo ""
exit 0


# vim:ai:et:ts=4:sw=4:sts=4:fenc=utf-8:
