#!/bin/bash
clear

set -e

sudo apt-get install virtualenv python3-pip libpq-dev python3-dev -y

mkdir -pv ~/python-work
cd ~/python-work
virtualenv -p python3 pgadmin4
cd pgadmin4
source bin/activate

pip3 install https://ftp.postgresql.org/pub/pgadmin/pgadmin4/v2.1/pip/pgadmin4-2.1-py2.py3-none-any.whl
touch lib/python3.6/site-packages/pgadmin4/config_local.py
cat > lib/python3.6/site-packages/pgadmin4/config_local.py << EOF

import os
DATA_DIR = os.path.realpath(os.path.expanduser(u'~/.pgadmin/'))
LOG_FILE = os.path.join(DATA_DIR, 'pgadmin4.log')
SQLITE_PATH = os.path.join(DATA_DIR, 'pgadmin4.db')
SESSION_DB_PATH = os.path.join(DATA_DIR, 'sessions')
STORAGE_DIR = os.path.join(DATA_DIR, 'storage')
SERVER_MODE = False

print('DATA_DIR' + DATA_DIR)
print( ' LOG_FILE '+ LOG_FILE)
print(  'SQLITE_PATH ' +SQLITE_PATH )
print( ' SESSION_DB_PATH ' + SESSION_DB_PATH  )
print( ' STORAGE_DIR ' + STORAGE_DIR  )

print( ' SERVER_MODE ' + SERVER_MODE  )
EOF

cat > pgadmin4.sh << EOF

#!/bin/bash
cd ~/python-work/pgadmin4
source bin/activate
python3 lib/python3.6/site-packages/pgadmin4/pgAdmin4.py

EOF
