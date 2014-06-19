<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="html" indent="yes"/>
  <xsl:template match="/">
    <html>
      <body>
        <h2 style="background-color:#DDDDDD;color:#930000;border:2px solid #CACACA;">Summary Essays reports</h2>
        <p style="text-align:left;background-color:SlateGray; color:white;font-size:16">Files</p>
        <table>
          <xsl:for-each select="report/file">
          <xsl:sort select="info/@pagesBySymbols" order="descending"/>
            <xsl:variable name="name" select="@name"/>
            <tr>
              <td style="border:2px solid #CACACA">
                <a href="{$name}">
                  <xsl:value-of select="@name"/>
                </a>
                 <br/>Has <xsl:value-of select="info/@symbols"/> symbols <xsl:value-of select="info/@sentances"/> sentances and <xsl:value-of select="info/@allWords"/> words.
                 Pages based on symbols count: <xsl:value-of select="info/@pagesBySymbols"/> , pages based on words count <xsl:value-of select="info/@pagesByWords"/>
              </td>
            </tr>
          </xsl:for-each>
        </table>  
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>
