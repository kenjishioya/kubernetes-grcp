FROM python:3.9

COPY . /src
WORKDIR /src

ENV GRPC_PYTHON_VERSION 1.42.0
RUN python -m pip install --upgrade pip
RUN pip install grpcio==${GRPC_PYTHON_VERSION} grpcio-tools==${GRPC_PYTHON_VERSION}

ENTRYPOINT ["python", "app.py"]