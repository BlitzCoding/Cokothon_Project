import warnings
import pandas as pd
import re
from bs4 import BeautifulSoup
import requests
from tqdm.auto import tqdm
import time
from datetime import date
import gspread
from oauth2client.service_account import ServiceAccountCredentials
today = date.today()

warnings.filterwarnings('ignore')

# google sheet 연동
scope = [
    'https://spreadsheets.google.com/feeds',
    'https://www.googleapis.com/auth/drive',
]
json_file_name = 'crawling_csv\lithe-elixir-373008-e1b38f977717.json'
credentials = ServiceAccountCredentials.from_json_keyfile_name(
    json_file_name, scope)
gc = gspread.authorize(credentials)
spreadsheet_url = 'https://docs.google.com/spreadsheets/d/1OCjhJQDGeLrI_dvpeKd6xI14zAPmIMX9dg4o7jPVA28/edit#gid=0'
doc = gc.open_by_url(spreadsheet_url)
worksheet = doc.worksheet('시트1')


# 메인 페이지
def page_crawler():
    link_url = r'https://www.bizinfo.go.kr/web/lay1/bbs/S1T122C128/AS/74/'

    for data in soup.find('tbody').find_all('tr'):
        # 지원분야
        cat_temp = data.select('td')[1].text.strip()
        cat_lst.append(cat_temp)

        # 지원사업명
        name_temp = data.select('td')[2].find('a').text.strip()
        name_lst.append(name_temp)

        # 링크
        link_temp = data.select('td')[2].find('a')['href']
        link_temp = link_url + link_temp
        link_lst.append(link_temp)

        # 신청기간
        period_temp = data.select('td')[3].text.strip()
        period_lst.append(period_temp)

        # 등록일
        update_temp = data.select('td')[5].text.strip()
        update_lst.append(update_temp)

# 세부 페이지


def target_page_crawler():
    for i in range(len(link_lst)):
        tareget_response = requests.get(link_lst[i])
        target_html = tareget_response.text
        soup = BeautifulSoup(target_html, 'html.parser')

        # 사업개요_신청 자격
        apply_temp = soup.find('div', {'class': 'view_cont'}).find_all('li')[
            3].find('div').text.split('☞')[1].strip()
        apply_lst.append(apply_temp)

        # 사업개요_지원 사항
        benefit_temp = soup.find('div', {'class': 'view_cont'}).find_all('li')[
            3].find('div').text.split('☞')[2].strip()
        benefit_lst.append(benefit_temp)


url_temp = r'https://www.bizinfo.go.kr/web/lay1/bbs/S1T122C128/AS/74/list.do?hashCode=09&rowsSel=6&rows=15&cpage'
# 실행 횟수 지정

# 마지막 페이지까지 반복하도록
iteration_num = 6

# 지원분야
cat_lst = []
# 지원사업명
name_lst = []
# 링크
link_lst = []
# 신청기간
period_lst = []
# 등록일
update_lst = []
# 사업개요_신청 자격
apply_lst = []
# 사업개요_지원 사항
benefit_lst = []

tik = time.time()

for num in tqdm(range(iteration_num)):
    url_path = url_temp + str(num + 1)

    response = requests.get(url_path)
    html = response.text
    soup = BeautifulSoup(html, 'html.parser')
    time.sleep(1)

    # 페이지 크롤링
    page_crawler()

print('main page crawled')

# 메인 페이지 종료 이후, 세부 페이지 크롤링
target_page_crawler()

# 크롤링 종료
print("---{}s seconds done---".format(time.time() - tik))

# dataframe 저장
df = pd.DataFrame({'category': cat_lst,
                   'name': name_lst,
                   'period': period_lst,
                   'apply': apply_lst,
                   'benefit': benefit_lst,
                   'link': link_lst,
                   'update': update_lst})
df.index = df.index+1

# spreadsheet에 df파일 쓰기


def set_with_dataframe(worksheet, df, include_index=True):
    (start_row, start_col) = (2, 1)  # A1부터 시작
    end_row = start_row + len(df.index) - 1  # 행의 개수
    end_col = start_col + len(df.columns) - 1  # 열의 개수
    cell_list = worksheet.range(
        start_row, start_col, end_row, end_col)  # 범위 지정
    for cell in cell_list:  # 셀의 위치에 맞게 데이터 입력
        val = df.iloc[cell.row - start_row,
                      cell.col - start_col]  # 행과 열의 위치에 맞는 데이터 입력
        cell.value = val  # 셀에 값 입력
    worksheet.update_cells(cell_list)  # 범위에 있는 셀 업데이트


set_with_dataframe(worksheet, df)  # df를 spreadsheet에 쓰기

df.to_csv('검색결과.csv', encoding='utf-8-sig')

# dataframe 저장 완료
print('저장 완료')
