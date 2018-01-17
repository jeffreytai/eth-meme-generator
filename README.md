# eth-meme-generator

Ethereum price tracker which retrieves the current price from Crytocompare. Historical prices at $100-increments are stored in a MySQL database.

Whenever a new all-time high has been reached by at least $100 from the previous increment, generate a meme using Cloudinary, store it locally, and upload the file to a designated channel on Slack.
