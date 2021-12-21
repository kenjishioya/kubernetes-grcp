from grpc_server.server import serve
import logging
import sys

if __name__ == '__main__':
    logging.basicConfig()
    console = logging.StreamHandler(stream=sys.stdout)
    # set a format which is simpler for console use
    formatter = logging.Formatter('%(name)-12s: %(levelname)-8s %(message)s')
    console.setFormatter(formatter)
    # add the handler to the root logger
    logger = logging.getLogger()
    logger.addHandler(console)
    logger.setLevel(logging.DEBUG)
    serve()