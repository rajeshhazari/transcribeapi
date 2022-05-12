#!/bin/bash
clear

sudo apt-get install virtualenv python3-pip libpq-dev python3-dev

mkdir -pv ~/python-work
cd ~/python-work
virtualenv -p python3 pgadmin4
cd pgadmin4
source bin/activate

pip3 install https://ftp.postgresql.org/pub/pgadmin/pgadmin4/v2.1/pip/pgadmin4-2.1-py2.py3-none-any.whl

cat > lib/python3.6/site-packages/pgadmin4/config_local.py << EOF

import os
DATA_DIR = os.path.realpath(os.path.expanduser(u'~/.pgadmin/'))
LOG_FILE = os.path.join(DATA_DIR, 'pgadmin4.log')
SQLITE_PATH = os.path.join(DATA_DIR, 'pgadmin4.db')
SESSION_DB_PATH = os.path.join(DATA_DIR, 'sessions')
STORAGE_DIR = os.path.join(DATA_DIR, 'storage')
SERVER_MODE = False

EOF

#!/bin/bash
cd ~/pgadmin4
source bin/activate
python3 lib/python3.6/site-packages/pgadmin4/pgAdmin4.py
