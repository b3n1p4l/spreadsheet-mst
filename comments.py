import praw
from praw.models import MoreComments

# keyword list
keywords = ('DIS', 'disney', 'Disney', 'DISNEY', 'ATVI', 'Activision', 'activision', 'ACTIVISION')

def main():
    # praw config
    reddit = praw.Reddit(client_id='dz6xY2UJLsEGtQ',
                        client_secret='TnmT7AmZr2XDZKMESAYhmnB59Qk',
                        redirect_uri='http://localhost:8080',
                        user_agent='testscript by /u/fakebot3')
    reddit.read_only = True
    
    # submission URL
    submission = reddit.submission(url='https://www.reddit.com/r/wallstreetbets/comments/fhnldz/what_are_your_moves_tomorrow_march_13_2020/')

    #loop
    for top_level_comment in submission.comments:
        if isinstance(top_level_comment, MoreComments):
            more_comments(top_level_comment)
        else:
            output(top_level_comment)

def more_comments(mc):
    for cmt in mc.comments():
        if isinstance(cmt, MoreComments):
            more_comments(cmt)
        else:
            output(cmt)

def output(cmt):
    comment = cmt.body
    if any(word in comment for word in keywords):
        print('----------------------')
        print(comment)
        print('----------------------')

main()
# todo: parse into dict search which data type is best for searching for strings or just perform in place string comparison and then print out