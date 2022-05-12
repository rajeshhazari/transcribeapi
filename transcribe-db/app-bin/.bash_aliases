# Smart ls alias
alias lah='ls -lah'

# Make and change directory at once
alias mkcd='_(){ mkdir -p $1; cd $1; }; _'

# fast find
alias ff='find . -name $1'

# change directories easily
alias ..='cd ..'
alias ...='cd ../..'
alias ....='cd ../../..'
alias .....='cd ../../../..'

# apt get distro specific  - Debian / Ubuntu and friends #
# install with apt-get
alias apt-get='sudo apt-get'
alias updateupgradey="sudo apt-get update && sudo apt-get upgrade --yes"

# update on one command
alias update='sudo apt-get update && sudo apt-get upgrade'


## set some other defaults ##
alias df='df -H'
alias du='du -ch'

# top is atop, just like vi is vim
alias top='atop'

## this one saved by butt so many times ##
alias wget='wget -c'

## pass options to free ##
alias meminfo='free -m -l -t'

## get top process eating memory
alias psmem='ps auxf | sort -nr -k 4'
alias psmem10='ps auxf | sort -nr -k 4 | head -10'

## get top process eating cpu ##
alias pscpu='ps auxf | sort -nr -k 3'
alias pscpu10='ps auxf | sort -nr -k 3 | head -10'

## Get server cpu info ##
alias cpuinfo='lscpu'

## older system use /proc/cpuinfo ##
##alias cpuinfo='less /proc/cpuinfo' ##

## get GPU ram on desktop / laptop##
alias gpumeminfo='grep -i --color memory /var/log/Xorg.0.log'


## All of our servers eth1 is connected to the Internets via vlan / router etc  ##
alias dnstop='dnstop -l 5  eth1'
alias vnstat='vnstat -i eth1'
alias iftop='iftop -i eth1'
alias tcpdump='tcpdump -i eth1'
alias ethtool='ethtool eth1'

# work on wlan0 by default #
# Only useful for laptop as all servers are without wireless interface
alias iwconfig='iwconfig wlan0'


alias ports='netstat -tulanp'

# Stop after sending count ECHO_REQUEST packets #
alias ping='ping -c 5'
# Do not wait interval 1 second, go fast #
alias fastping='ping -c 100 -s.2'

alias path='echo -e ${PATH//:/\\n}'
alias now='date +"%T"'
alias nowtime=now
alias nowdate='date +"%d-%m-%Y"'

# handy short cuts #
alias h='history'
alias j='jobs -l'
alias c='clear'

# install  colordiff package :)
alias diff='colordiff'

alias mkdir='mkdir -pv'

alias sha1='openssl sha1'
## Colorize the grep command output for ease of use (good for log files)

alias egrep='egrep --color=auto'
alias fgrep='fgrep --color=auto'

## Colorize the ls output ##
alias ls='ls --color=auto'

## Use a long listing format ##
alias ll='ls -la'

## Show hidden files ##
alias l.='ls -d .* --color=auto'

alias python='/usr/bin/python3.8'
python --version

sha256() { echo -n "$*" | shasum -a 256 ;}
NOW=$(date +"%d-%m-%Y")
echo $NOW

alias sshrajraspi01='ssh  rajraspiadm@rajraspiiot01 -p 2121'
alias sshdevuserappserver='ssh  devuser@devappserver-api -p 2121'
#alias grep='grep -v grep | '
alias sshdevuserappserver138='ssh  devuser@192.168.1.138 -p 2121'
alias cdhomeproject='cd /mnt/Java/home-projects/ && ls -lrth'
alias sshdevuserqaappserver='ssh  devuser@qa-appserver-api.com -p 2121'
alias sshrootqaappser239='ssh  devuser@192.168.1.239 -p 21 '


function extract() {

	if [[ "$#" -lt 1 ]]; then
	  echo "Usage: extract <path/file_name>.<zip|rar|bz2|gz|tar|tbz2|tgz|Z|7z|xz|ex|tar.bz2|tar.gz|tar.xz>"
	  return 1 #not enough args
	fi

	if [[ ! -e "$1" ]]; then
	  echo -e "File does not exist!"
	  return 2 # File not found
	fi

	if [[ ! -z "$2" ]]; then
	  echo "Destination dir :: $2"
	  DESTDIR=$2
    else
        DESTDIR="."
	fi

	filename=`basename "$1"`

	case "${filename##*.}" in
	  tar)
		echo -e "Extracting $1 to $DESTDIR: (uncompressed tar)"
		tar xvf "$1" -C "$DESTDIR"
		;;
	  gz)
		echo -e "Extracting $1 to $DESTDIR: (gip compressed tar)"
		tar xvfz "$1" -C "$DESTDIR"
		;;
	  tgz)
		echo -e "Extracting $1 to $DESTDIR: (gip compressed tar)"
		tar xvfz "$1" -C "$DESTDIR"
		;;
	  xz)
		echo -e "Extracting  $1 to $DESTDIR: (gip compressed tar)"
		tar xvf -J "$1" -C "$DESTDIR"
		;;
	  bz2)
		echo -e "Extracting $1 to $DESTDIR: (bzip compressed tar)"
		tar xvfj "$1" -C "$DESTDIR"
		;;
      tbz2)
	  	echo -e "Extracting $1 to $DESTDIR: (tbz2 compressed tar)"
	  	tar xvjf "$1" -C "$DESTDIR"
		;;
	  zip)
		echo -e "Extracting $1 to $DESTDIR: (zipp compressed file)"
		unzip "$1" -d "$DESTDIR"
		;;
	  lzma)
	  	echo -e "Extracting $1 : (lzma compressed file)"
		unlzma "$1"
		;;
	  rar)
		echo -e "Extracting $1 to $DESTDIR: (rar compressed file)"
		unrar x "$1" "$DESTDIR"
		;;
	  7z)
		echo -e  "Extracting $1 to $DESTDIR: (7zip compressed file)"
		7za e "$1" -o "$DESTDIR"
		;;
	  xz)
	  	echo -e  "Extracting $1 : (xz compressed file)"
	    unxz  "$1"
	  	;;
	  exe)
	   	cabextract "$1"
	  	;;
	  *)
		echo -e "Unknown archieve format!"
		return
	  	;;
	esac
}

alias g='git status '
alias ga='git add'
alias gr='git rm'

# Npm alias
alias np='npm'

# Npm install alias
alias npi='npm install'
alias npis='npm install --save'
alias npig='npm install -g'

# Npm update
alias npu='npm update'
alias npug='npm update -g'

# Npm search
alias nps='npm search'

dcstop() { docker ps -a; docker container stop $1; docker container  rm $1; }; alias dcstop=dcstop;

alias proxyon="export http_proxy='http://devappserver-api:8888';export https_proxy='http://devappserver-api:8888'"
alias proxyoff="export http_proxy='';export https_proxy=''"

PYTHON_VERSION=$(python --version)
#echo $PYTHON_VERSION
#if [ -z $PYTHON_VERSION ]; then
#	PYTHON3_HOME="/usr/bin/python3.6";
#		if [ -d $PYTHON3_HOME ]; then
#			alias python=$PYTHON3_HOME
#		fi
#	else
#		python --version
#fi
ls -lrth /usr/bin/python3
python3 -V

gen_passwd() {
    local length=$1
    local charset="$2"
    local password=""
    while [ ${#password} -lt "$length" ]
    do
        password=$(echo "$password""$(head -c 100 /dev/urandom | LC_ALL=C tr -dc "$charset")" | fold -w "$length" | head -n 1)
    done
    echo "$password"
}
