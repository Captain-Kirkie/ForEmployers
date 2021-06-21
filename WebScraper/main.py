# This is a sample Python script.

# Press ⌃R to execute it or replace it with your code.
# Press Double ⇧ to search everywhere for classes, files, tool windows, actions, and settings.
# https://realpython.com/python-web-scraping-practical-introduction/#scrape-and-parse-text-from-websites
#  beautiful soup is a html parser designed for html parsing
#  https://www.youtube.com/watch?v=B1IsCbXp0uE
#  https://analyticsindiamag.com/mechanicalsoup-web-scraping-custom-dataset-tutorial/
import re
from urllib.request import urlopen
from bs4 import BeautifulSoup
import mechanicalsoup
import smtplib
from email.message import EmailMessage

password = "bottleNeck2021"
email = "kirktest93@gmail.com"
google_password = "eiuzcjohoagsvnsk"

company_career_dict = {"BambooHR": "https://company.bamboohr.com/jobs/?source=bamboohr",
                       "Adobe": "https://adobe.wd5.myworkdayjobs.com/external_experienced",
                       "Recursion": "https://boards.greenhouse.io/recursionpharmaceuticals",
                       "The Front": "https://thefrontclimbingclub.com/careers/"}
test_career_dict = {"The Front": "https://thefrontclimbingclub.com/careers/"}


def email_alert(subject, body, to):
    msg = EmailMessage()
    msg.set_content(body)
    msg['subject'] = subject
    msg['to'] = to
    msg['from'] = email
    server = smtplib.SMTP("smtp.gmail.com", 587)  # set server to googles server
    server.starttls()
    server.login(email, google_password)
    server.send_message(msg)
    server.quit()

def empty_list(list):
    return len(list) > 0


def search_company_openings(company_name, key_word):
    # dictionary of company name and url to employment page
    key_word = key_word.strip()
    company_name = company_name.strip()
    if company_name in company_career_dict:
        url = company_career_dict.get(company_name)
    else:
        raise Exception("Company Name does Not exist " + company_name)

    browser = mechanicalsoup.Browser()  # now I have a browser

    if url is None:  # throw error if company name does not exist
        raise Exception("Company Name does Not exist " + company_name)

    page = browser.get(url)
    return page.soup(text=lambda t: key_word.strip() in t)  # search for keyword
    #  https://stackoverflow.com/questions/8936030/using-beautifulsoup-to-search-html-for-string


def search_job_site_notify(dictionary):
    keyword_dict = {"Intern", "Software Engineer", "Android Developer", "iOS Developer"}
    openings = []
    position_url = {}
    #  search every keyword

    for key_word in keyword_dict:
        for company in dictionary.keys():
            job_openings = search_company_openings(company, key_word)
            if empty_list(job_openings):
                openings.append(job_openings)
                position_url[company] = company_career_dict.get(company)
    if empty_list(position_url):  # in every company
        print("Sending Email and Text")
        email_alert("jobs", str(position_url), "7155772551@txt.att.net")


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
   # email_alert("jobs", "helloKirk", "7155772551@txt.att.net")
    search_job_site_notify(company_career_dict)


# See PyCharm help at https://www.jetbrains.com/help/pycharm/
