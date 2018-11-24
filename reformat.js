#!/usr/bin/nodejs

fs = require('fs');
file = fs.readFileSync(process.argv[2]);
console.log(JSON.stringify(JSON.parse(file)));

