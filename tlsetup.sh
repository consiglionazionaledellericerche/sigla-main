#!/usr/bin/env bash
#
# Minimal Travis Set-up Script
#
# This script sets-up a minimal TexLive 2015 distribution with the
# additional packages being installed when needed. This is intended
# for Travis continuous integration and is expected to work with the
# Ubuntu Trusty (14.04) Distribution
#
# Copyright (C) 2018 Lester James V. Miranda <ljvmiranda@gmail.com>
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
#

# Update aptitude repository
sudo apt-get -qq update

# Install TexLive 2015 Distribution
# This includes the recommended fonts, LaTeX packages, and the XeLaTeX
# compiler
sudo apt-get install -y --no-install-recommends texlive-fonts-recommended \
    texlive-latex-extra \
    texlive-fonts-extra \
    dvipng \
    texlive-latex-recommended \
    texlive-xetex

# Install decompression tool to extract LaTex packages later on
sudo apt-get install -y xzdec

# Install biber for .bib compilation
sudo apt-get install -y biber

# Initialize user tree for installing additional packages
sudo tlmgr init-usertree

# Define repository for obtaining LaTeX packages (using TexLive 2015)
# sudo tlmgr option repository ftp://tug.org/historic/systems/texlive/2015/tlnet-final

# ----------------------------------------------------------------------------
#   INSTALL ADDITIONAL PACKAGES HERE
# ----------------------------------------------------------------------------

sudo tlmgr install \
    hyperref       \
    parskip        \
    babel          \
    tools          \
    graphics       \
    xcolor
