function transable(url) {
  return url.startsWith("http://www.hnyanling.gov.cn")
}

async function fetch_page(url) {
  const req = await fetch(url)
  if (req.status != 200)
    throw new Error(url + " responses " + req.status)

  const page = document.createElement('html')
  page.innerHTML = await req.text()
  
  return page
}

function fetchNewsList() {
  const result = []

  function fetch_hnyanling() {
    async function fetch_text_news(url) {
      const page = await fetch_page(url)

      for (let x of page.querySelector("div.c > ul").children) {
        result.push({
          url: x.getAttribute('href'),
          title: x.querySelector('i').textContent,
          date: x.querySelector('em').textContent,
          source: 'hnyanling'
        })
      }
    }

    async function fetch_video_news(url) {
      const page = await fetch_page(url)

      for (let x of page.querySelector("div.video-c > ul").children) {
        result.push({
          url: x.getAttribute('href'),
          title: x.querySelector('i').textContent,
          date: x.querySelector('em').textContent,
          thumbnail: "http://www.hnyanling.gov.cn" + x.querySelector('img').getAttribute('src'),
          source: 'hnyanling'
        })
      }
    }

    return Promise.all([
      fetch_text_news("http://www.hnyanling.gov.cn/c53/index.html"),
      fetch_text_news("http://www.hnyanling.gov.cn/c56/index.html"),
      fetch_text_news("http://www.hnyanling.gov.cn/c57/index.html"),
      fetch_text_news("http://www.hnyanling.gov.cn/c58/index.html"),
      fetch_video_news("http://www.hnyanling.gov.cn/c539/index.html")
    ])
  }

  return Promise.all([fetch_hnyanling()]).then(x=>result)
}



window.scraper = {
  fetchNewsList
}