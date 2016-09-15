# Temperature Logger
![](docs/img.png)
---
This project presents a mobile application demo, a part of distributed system
consisted of:

* creator starter kit - CI40, clickers
* device server
* web application providing cloud storage
* mobile application

## About the application

Mobile app is only one piece of a puzzle and covers a few simple
use-cases:
* shows available sensors with current measurement readings,
* shows measured temperatures as a graph for last 1h, 12h day and full week,
* displays the 'delta' value for updating the IPSO temperature value,
* user can update the 'delta' value,
* user can clear all measurements.

### Communication
As it was mentioned above main responsibility of the mobile application is to
provide simple visualization of measured temperatures.
To achieve that mobile application communicates directly with web application
using REST API (HATEOAS constraints).

With this approach all heavy-lifting operations, communication with device server or
managing database was moved to a server side leaving communication as simple as possible.

## License
Copyright (c) 2016, Imagination Technologies Limited and/or its affiliated group companies.
All rights reserved.
Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the
following disclaimer.
2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
following disclaimer in the documentation and/or other materials provided with the distribution.
3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote
products derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
